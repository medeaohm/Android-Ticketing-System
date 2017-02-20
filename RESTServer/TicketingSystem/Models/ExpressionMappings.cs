using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using TicketingSystem.Data.Models;
using TicketingSystem.Models.Comments;
using TicketingSystem.Models.NewsModels;
using TicketingSystem.Models.Tickets;
using TicketingSystem.Models.Users;

namespace TicketingSystem.Models
{
    public static class ExpressionMappings
    {
        private static Expression<Func<Ticket, TicketResponseModel>> ticketExpression = ticket => new TicketResponseModel
        {
            Id = ticket.Id.ToString(),
            BoughtAt = ticket.BoughtAt,
            Cost = ticket.Cost,
            Expired = ticket.DateActivated.HasValue && ticket.ExpiresOn.HasValue && DateTime.Now > ticket.ExpiresOn.Value,
            DateActivated = ticket.DateActivated,
            Activated = ticket.DateActivated.HasValue,
            ExpiresOn = ticket.ExpiresOn,
            Duration = ticket.DurationInHours,
            QRCode = ticket.QRCode,
            Owner = new UserResponseModel
            {
                UserName = ticket.Owner.UserName,
                FullName = ticket.Owner.FirstName + " " + ticket.Owner.LastName,
                Id = ticket.Owner.Id,
            }
        };

        private static Expression<Func<User, UserViewModel>> userExpression = user => new UserViewModel()
        {
            Email = user.Email,
            FirstName = user.FirstName,
            FullName = user.FirstName + " " + user.LastName,
            Tickets = user.Tickets.AsQueryable().Select(ticketExpression),
            LastName = user.LastName,
            UserName = user.UserName,
            Avatar = user.Avatar,
            FileName = user.AvatarFileName,
            Balance = Math.Round(user.Balance, 2),
            Id = user.Id,
        };

        private static Expression<Func<Comment, CommentViewModel>> commentExpression = comment => new CommentViewModel() {
            Id = comment.Id,
            Content = comment.Content,
            CreatedOn = comment.CreatedOn,
            Author = comment.Author.UserName,
            //Author = new UserResponseModel {
            //    UserName = comment.Author.UserName,
            //    FullName = comment.Author.FirstName + " " + comment.Author.LastName,
            //    Id = comment.Author.Id,
            //},
            NewsItemId = comment.NewsItemId
        };

        private static Expression<Func<News, NewsViewModel>> newsExpression = news => new NewsViewModel() {
            Id = news.Id,
            Title = news.Title,
            Content = news.Content,
            CreatedOn = news.CreatedOn,
            Comments = news.Comments.AsQueryable().Select(commentExpression)
        };

        private static IEnumerable<CommentViewModel> MapCommentsToViewModel(ICollection<Comment> comments)
        {
            var commentsViewModel = new List<CommentViewModel>();
            var c = comments.ToList();
            foreach (var comment in c)
            {
                var currentComment = MapCommentToViewModel(comment);
                commentsViewModel.Add(currentComment);
            }

            return commentsViewModel;
        }

        public static IQueryable<TicketResponseModel> MapTicketsToViewModels(this IQueryable<Ticket> tickets)
        {
            return tickets.Select(ticketExpression);
        }

        public static TicketResponseModel MapTicketToViewModel(this Ticket ticket)
        {
            if (ticket == null)
            {
                return null;
            }

            return ticketExpression.Compile().Invoke(ticket);
        }

        public static IQueryable<UserViewModel> MapUsersToViewModels(this IQueryable<User> users)
        {
            return users.Select(userExpression);
        }

        public static UserViewModel MapUserToViewModel(this User user)
        {
            if (user == null)
            {
                return null;
            }

            return userExpression.Compile().Invoke(user);
        }

        public static IQueryable<CommentViewModel> MapCommentsToViewModels(this IQueryable<Comment> comments)
        {
            return comments.Select(commentExpression);
        }

        public static CommentViewModel MapCommentToViewModel(this Comment comment)
        {
            if (comment == null)
            {
                return null;
            }

            return commentExpression.Compile().Invoke(comment);
        }

        public static IQueryable<NewsViewModel> MapNewsToViewModels(this IQueryable<News> news)
        {
            return news.Select(newsExpression);
        }

    }
}