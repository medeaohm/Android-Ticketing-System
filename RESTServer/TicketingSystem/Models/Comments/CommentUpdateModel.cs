using System;
using System.ComponentModel.DataAnnotations;

namespace TicketingSystem.Models.Comments
{
    public class CommentUpdateModel
    {
        public int Id { get; set; }

        [Required]
        [MaxLength(100000, ErrorMessage = "The comment is too long")]
        public string Content { get; set; }

        public string AuthorId { get; set; }

        public DateTime ModifiedOn { get; set; }
    }
}