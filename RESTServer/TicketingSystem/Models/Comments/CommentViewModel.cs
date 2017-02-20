using System;

using TicketingSystem.Models.Users;

namespace TicketingSystem.Models.Comments
{
    public class CommentViewModel
    {
        public int Id { get; set; }

        public string Content { get; set; }

        public string Author { get; set; }

        public DateTime CreatedOn { get; set; }

        public int NewsItemId { get; set; }
    }
}