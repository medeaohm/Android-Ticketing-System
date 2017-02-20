using System.Linq;

using TicketingSystem.Data.Models;

namespace TicketingSystem.Services
{
    public interface INewsService
    {
        News GetById(string id);

        News GetById(int? id);

        IQueryable<News> GetAll();

        IQueryable<News> GetMostRecent(int count);

        IQueryable<News> GetByUserId(string userId);

        void Update();

        void Add(News news);

        void Delete(int id);
    }
}