namespace TicketingSystem.Services
{
    using System.Linq;
    using Providers;
    using Data.Models;
    using Repositories;
    using System;

    public class NewsService : INewsService
    {
        private readonly IIdentifierProvider identifierProvider;
        private IDeletableEntityRepository<News> news;

        public NewsService(IDeletableEntityRepository<News> news, IIdentifierProvider identifierProvider)
        {
            this.news = news;
            this.identifierProvider = identifierProvider;
        }

        public News GetById(string id)
        {
            var intId = this.identifierProvider.DecodeId(id);
            var news = this.news.GetById(intId);
            return news;
        }

        public News GetById(int? id)
        {
            return this.news.GetById(id);
        }

        public IQueryable<News> GetAll()
        {
            return this.news.All().OrderByDescending(p => p.CreatedOn);
        }

        public IQueryable<News> GetMostRecent(int count)
        {
            return this.news.All().OrderByDescending(p => p.CreatedOn).Take(count);
        }

        public void Update()
        {
            this.news.SaveChanges();
        }

        public void Add(News post)
        {
            this.news.Add(post);
        }

        public IQueryable<News> GetByUserId(string userId)
        {
            throw new NotImplementedException();
        }

        public void Delete(int id)
        {
            throw new NotImplementedException();
        }
        /*
public void Delete(int id)
{
   var news = this.news.GetById(id);
   foreach (var comment in news.Comments)
   {
   }

   foreach (var image in post.Gallery)
   {
       image.IsDeleted = true;
   }

   post.IsDeleted = true;
}
*/
    }
}
