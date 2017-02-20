using System;
using System.Collections.Generic;
using System.Linq;
using TicketingSystem.Data.Models;
using TicketingSystem.Models.Comments;

namespace TicketingSystem.Models.NewsModels
{
    public class NewsViewModel
    {
        public NewsViewModel()
        {
        }

        public int Id { get; set; }

        public string Title { get; set; }

        public string Content { get; set; }

        public DateTime CreatedOn { get; set; }

        public IEnumerable<CommentViewModel> Comments { get; set; }
    }
}