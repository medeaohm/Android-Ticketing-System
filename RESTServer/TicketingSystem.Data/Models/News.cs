using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

using TicketingSystem.Data.Constants;

namespace TicketingSystem.Data.Models
{
    public class News : BaseModel<int>
    {
        private ICollection<Comment> comments;

        public News()
        {
            this.comments = new HashSet<Comment>();
        }

        [MinLength(DataModelConstants.StringMinLength)]
        [MaxLength(DataModelConstants.StringShortMaxLength)]
        public string Title { get; set; }

        [MinLength(DataModelConstants.StringMinLength)]
        [MaxLength(DataModelConstants.StringLongMaxLength)]
        public string Content { get; set; }

        [MaxLength(DataModelConstants.StringShortMaxLength)]
        public string AuthorId { get; set; }

        public virtual User Author { get; set; }

        public virtual ICollection<Comment> Comments
        {
            get { return this.comments; }
            set { this.comments = value; }
        }
    }
}
