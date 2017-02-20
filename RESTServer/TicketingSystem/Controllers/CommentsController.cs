using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Cors;

using TicketingSystem.Data;
using TicketingSystem.Data.Models;
using TicketingSystem.Models;
using TicketingSystem.Models.Comments;


using Microsoft.AspNet.Identity;
using System;
using TicketingSystem.Services;

namespace TicketingSystem.Controllers
{
    [EnableCors("*", "*", "*")]
    public class CommentsController : ApiController
    {
        private ITicketingSystemContext context;

        public CommentsController(ITicketingSystemContext context)
        {

            this.context = context;
        }

        public CommentsController() : this(new TicketingSystemContext())
        {
        }


        [HttpGet]
        public IHttpActionResult GetAll()
        {
            IEnumerable<CommentViewModel> c = this.context.Comments.MapCommentsToViewModels().ToList();
            return this.Json(c);
        }

        [HttpGet]
        [Route("api/Comments/ByNews/{id}")]
        public IHttpActionResult GetByNewsId(int id)
        {
            IEnumerable<CommentViewModel> comment = this.context.Comments.MapCommentsToViewModels().ToList().Where(c => c.NewsItemId == id);


            return this.Json(comment);
        }

        //[Authorize]
        [HttpPost]
        [Route("api/Comments/Post")]
        public IHttpActionResult Post(CommentCreateModel comment)
        {
            var userId = this.context.Users.FirstOrDefault(u => u.UserName == comment.AuthorUsername).Id;

            if (comment != null && this.ModelState.IsValid)
            {
                var databaseComment = new Comment
                {
                    Content = comment.Content,
                    NewsItemId = comment.NewsItemId,
                    AuthorId = userId,
                    CreatedOn = DateTime.Now
                };

                context.Comments.Add(databaseComment);
                context.SaveChanges();

                return Ok(new
                {
                    id = databaseComment.Id,
                    content = databaseComment.Content,
                    author = databaseComment.Author.UserName,
                    createdOn = databaseComment.CreatedOn,
                });
            }
            else
            {
                return this.BadRequest(this.ModelState);
            }
        }

        [HttpPost]
        [Route("api/Comments/Delete/{id}")]
        public IHttpActionResult Delete(int id)
        {
            var commentToDelete = this.context.Comments.FirstOrDefault(c => c.Id == id);
            context.Comments.Remove(commentToDelete);
            context.SaveChanges();

            return Ok();
        }

        [HttpPut]
        public IHttpActionResult Update(CommentUpdateModel comment)
        {
            string currentUserId = this.User.Identity.GetUserId();

            if (comment != null && this.ModelState.IsValid && currentUserId == comment.AuthorId)
            {
                var commentToUpdate = this.context.Comments.FirstOrDefault(c => c.Id == comment.Id);

                commentToUpdate.Content = comment.Content;
                commentToUpdate.ModifiedOn = comment.ModifiedOn;

                context.SaveChanges();

                return Ok();
            }
            else
            {
                return this.BadRequest(this.ModelState);
            }
        }
    }
}
