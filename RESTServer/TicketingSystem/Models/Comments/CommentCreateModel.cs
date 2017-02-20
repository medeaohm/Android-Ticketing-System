using System;
using System.ComponentModel.DataAnnotations;

namespace TicketingSystem.Models.Comments
{
    public class CommentCreateModel
    {
        [Required]
        [MaxLength(100000, ErrorMessage = "The comment is too long")]
        public string Content { get; set; }

        public string AuthorUsername { get; set; }

        public int NewsItemId { get; set; }

        //public DateTime CreatedOn { get; set; }
    }
}