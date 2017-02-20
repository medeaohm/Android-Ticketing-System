using System;
using System.ComponentModel.DataAnnotations;

namespace TicketingSystem.Models.NewsModels
{
    public class NewsCreateModel
    {
        [Required]
        [MinLength(5, ErrorMessage = "The Title should be at least 5 symbols")]
        [MaxLength(100, ErrorMessage = "The Title should be maximum 100 symbols")]
        public string Title { get; set; }

        [Required]
        [MinLength(5, ErrorMessage = "The content should be at least 5 symbols")]
        public string Content { get; set; }

        public DateTime CreatedOn { get; set; }
    }
}