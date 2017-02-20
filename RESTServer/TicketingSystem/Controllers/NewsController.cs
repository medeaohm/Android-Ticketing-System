using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Cors;

using TicketingSystem.Data;
using TicketingSystem.Data.Constants;
using TicketingSystem.Data.Models;
using TicketingSystem.Models;
using TicketingSystem.Models.NewsModels;

namespace TicketingSystem.Controllers
{
    [EnableCors("*", "*", "*")]
    //[RoutePrefix("api/news")]
    public class NewsController : ApiController
    {
        private ITicketingSystemContext context;

        public NewsController(ITicketingSystemContext context)
        {
            this.context = context;
        }

        public NewsController()
            : this(new TicketingSystemContext())
        {
        }

        [HttpGet]
        public IHttpActionResult All()
        {
            IEnumerable<NewsViewModel> newsViewModels = this.context.News.MapNewsToViewModels().ToList();
            return this.Json(newsViewModels);
        }

        [HttpGet]
        public IHttpActionResult GetByID(int id)
        {
            var newsViewModels = this.context.News.MapNewsToViewModels().ToList().Find(c => c.Id == id);
            if (newsViewModels == null)
            {
                return NotFound();
            } 
            return this.Json(newsViewModels);
        }


        //[Authorize]
        [HttpPost]
        [Route("api/news/post")]
        public IHttpActionResult Post(NewsCreateModel news)
        {
            bool isAdmin = this.User.IsInRole(DataModelConstants.AdminRole);

            if (news != null && this.ModelState.IsValid && isAdmin)
            {
                var databaseNews = new News {
                    Content = news.Content,
                    Title = news.Title,
                    CreatedOn = DateTime.Now
                };

                context.News.Add(databaseNews);
                context.SaveChanges();

                return Ok(new
                {
                    id = databaseNews.Id,
                    content = databaseNews.Content,
                    title = databaseNews.Title,
                    createdOn = databaseNews.CreatedOn,
                });
            }
            else
            {
                return this.BadRequest(this.ModelState);
            }
        }

        [HttpPost]
        [Route("api/news/delete/{id}")]
        public IHttpActionResult Delete(int id)
        {
            var newsToDelete = this.context.News.FirstOrDefault(n => n.Id == id);
            var commentsToDelete = this.context.Comments.Where(c => c.NewsItemId == id);

            context.News.Remove(newsToDelete);
            foreach (var comment in commentsToDelete)
            {
                context.Comments.Remove(comment);
            }
           
            context.SaveChanges();

            return Ok();
        }
    }
}
