using System.Linq;
using TicketingSystem.Data.Models;
using TicketingSystem.Providers;
using TicketingSystem.Repositories;

namespace TicketingSystem.Services
{
    public class CommentService : ICommentService
    {
        private readonly IIdentifierProvider identifierProvider;
        private IDeletableEntityRepository<Comment> comments;

        public CommentService(IDeletableEntityRepository<Comment> comments, IIdentifierProvider identifierProvider)
        {
            this.comments = comments;
            this.identifierProvider = identifierProvider;
        }

        public Comment GetById(string id)
        {
            var intId = this.identifierProvider.DecodeId(id);
            var comment = this.comments.GetById(intId);
            return comment;
        }

        public Comment GetById(int? id)
        {
            var comment = this.comments.GetById(id);
            return comment;
        }

        public IQueryable<Comment> GetAll()
        {
            return this.comments.All().OrderBy(c => c.NewsItemId).ThenBy(c => c.CreatedOn);
        }

        public IQueryable<Comment> GetByNewsId(int newsId)
        {
            return this.comments
                .All()
                .Where(c => c.NewsItemId == newsId)
                .OrderBy(c => c.CreatedOn).ThenBy(c => c.Id);
        }

        public IQueryable<Comment> GetByUserId(string userId)
        {
            return this.comments
                .All()
                .Where(c => c.AuthorId == userId)
                .OrderBy(c => c.CreatedOn).ThenBy(c => c.Id);
        }

        public void Update()
        {
            this.comments.SaveChanges();
        }

        public void Add(Comment comment)
        {
            this.comments.Add(comment);
        }

        public void Delete(int id)
        {
            this.comments.GetById(id).IsDeleted = true;
        }
    }
}
