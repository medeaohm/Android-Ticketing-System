using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Cors;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using TicketingSystem.Data;
using TicketingSystem.Data.Models;
using TicketingSystem.Models;
using TicketingSystem.Models.Users;

namespace TicketingSystem.Controllers
{
    [EnableCors("*", "*", "*")]
    [RoutePrefix("api/users")]
    public class UsersController : ApiController
    {
        private ITicketingSystemContext context;

        public UsersController(ITicketingSystemContext context)
        {
            this.context = context;
        }

        public UsersController()
            : this(new TicketingSystemContext())
        {
        }

        [HttpGet]
        public IHttpActionResult All()
        {
            IEnumerable<UserViewModel> userModels = this.context.Users.MapUsersToViewModels().ToList();
            UserManager<User> userManager = this.GetUserManager();
            foreach (UserViewModel userViewModel in userModels)
            {
                userViewModel.Roles = userManager.GetRoles(userViewModel.Id);
            }

            return this.Json(userModels);
        }

        [HttpGet]
        [Authorize]
        [Route("Info")]
        public IHttpActionResult Info()
        {
            string currentUserId = this.User.Identity.GetUserId();

            User currentUser = this.context.Users.Find(currentUserId);
            if (currentUser == null)
            {
                return this.BadRequest("Cannot find current user!");
            }

            UserViewModel model = currentUser.MapUserToViewModel();
            model.Roles = this.GetUserManager().GetRoles(currentUserId);
            return this.Json(model);
        }

        [HttpGet]
        [Route("ById")]
        public IHttpActionResult ById(string id)
        {
            if (string.IsNullOrEmpty(id))
            {
                return this.BadRequest("Id cannot be null or empty");
            }

            User user = this.context.Users.Find(id);

            if (user == null)
            {
                return this.BadRequest("Cannot find user with id: " + id);
            }
            UserViewModel viewModel = user.MapUserToViewModel();
            viewModel.Roles = this.GetUserManager().GetRoles(user.Id);
            return this.Json(viewModel);
        }

        [HttpPost]
        public IHttpActionResult Create(RegisterBindingModel registerUserModel)
        {
            if (!this.ModelState.IsValid)
            {
                return this.BadRequest(this.ModelState);
            }

            User userCreateModel = new User()
            {
                Email = registerUserModel.Email,
                FirstName = registerUserModel.FirstName,
                LastName = registerUserModel.LastName,
                UserName = registerUserModel.UserName,
            };

            UserManager<User> userManager = this.GetUserManager();
            IdentityResult result = userManager.Create(userCreateModel, registerUserModel.Password);
            if (!result.Succeeded)
            {
                return this.BadRequest(string.Join(" ", result.Errors));
            }
            
            UserResponseModel responseModel = this.context.Users.Find(userCreateModel.Id).MapUserToViewModel();
            return this.Created("/", responseModel);
        }

        [HttpPut]
        [Authorize]
        public IHttpActionResult Update(UpdateUserModel model)
        {
            if (!this.ModelState.IsValid)
            {
                return this.BadRequest(this.ModelState);
            }

            string userId = this.User.Identity.GetUserId();
            User user = this.context.Users.Find(userId);

            user.FirstName = model.FirstName;
            user.LastName = model.LastName;
            user.UserName = model.UserName;

            if (user.Email != model.Email)
            {
                user.Email =  model.Email;
            }

            this.context.SaveChanges();

            return this.Ok();
        }

        [HttpPut]
        [Authorize]
        [Route("Avatar")]
        public async Task<IHttpActionResult> Avatar()
        {
            string currentUserId =  this.User.Identity.GetUserId();

            if (!Request.Content.IsMimeMultipartContent())
            {
                throw new HttpResponseException(HttpStatusCode.UnsupportedMediaType);
            }
            
            MultipartMemoryStreamProvider filesReadToProvider = await Request.Content.ReadAsMultipartAsync();

            // only one image
            if (filesReadToProvider.Contents.Count < 1)
            {
                return this.BadRequest("Cannot find image");
            }

            string fileName = filesReadToProvider.Contents[0].Headers.ContentDisposition.FileName;
            byte[] fileBytes = await filesReadToProvider.Contents[0].ReadAsByteArrayAsync();
            User currentUser = this.context.Users.Find(currentUserId);
            currentUser.Avatar = fileBytes;
            currentUser.AvatarFileName = fileName.Replace("\"", string.Empty);
            this.context.SaveChanges();

            return this.Ok();
        }

        [HttpDelete]
        public IHttpActionResult Delete(UserDeleteModel model)
        {
            if (!this.ModelState.IsValid)
            {
                return this.BadRequest(this.ModelState);
            }


            User userToDelete = this.context.Users.Find(model.Id);
            if (userToDelete == null)
            {
                return this.BadRequest("Cannot find user with id: " + model.Id);
            }

            this.context.Users.Remove(userToDelete);
            this.context.SaveChanges();

            return this.Ok();
        }

        [Authorize]
        [HttpPut]
        [Route("Charge")]
        public IHttpActionResult ChargeAccount(ChargeAccountModel model)
        {
            if (!this.ModelState.IsValid)
            {
                return this.BadRequest(this.ModelState);
            }

            string currentUserId = this.User.Identity.GetUserId();
            User user = this.context.Users.Find(currentUserId);
            user.Balance += model.Amount;
            this.context.SaveChanges();

            return this.Ok();
        }

        private UserManager<User> GetUserManager()
        {
            UserManager<User> userManager = new UserManager<User>(new UserStore<User>(this.context as DbContext));
            userManager.UserValidator = new UserValidator<User>(userManager) { AllowOnlyAlphanumericUserNames = false, RequireUniqueEmail = true };

            return userManager;
        }
    }
}
