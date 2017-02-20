using System.Linq;

using TicketingSystem.Data.Models;

namespace TicketingSystem.Services
{
    public interface ICommentService
    {
        Comment GetById(string id);

        Comment GetById(int? id);

        IQueryable<Comment> GetAll();

        IQueryable<Comment> GetByNewsId(int newsId);

        IQueryable<Comment> GetByUserId(string userId);

        void Update();

        void Add(Comment comment);

        void Delete(int id);
    }
}
