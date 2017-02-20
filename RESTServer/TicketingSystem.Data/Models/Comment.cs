using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using TicketingSystem.Data.Constants;

namespace TicketingSystem.Data.Models
{
    public class Comment : BaseModel<int>
    {
        [MaxLength(DataModelConstants.StringLongMaxLength)]
        public string Content { get; set; }

        //[ForeignKey("Author")]
        public string AuthorId { get; set; }

        public virtual User Author { get; set; }

       // [ForeignKey("NewsItem")]
        public int NewsItemId { get; set; }

        public virtual News NewsItem { get; set; }
    }
}
