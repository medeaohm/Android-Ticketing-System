using System.ComponentModel.DataAnnotations;
using TicketingSystem.Constants;

namespace TicketingSystem.Models.Users
{
    public class UpdateUserModel
    {
        [Required]
        public string Id { get; set; }

        [Required]
        public string UserName { get; set; }

        [Required]
        public string FirstName { get; set; }

        [Required]
        public string LastName { get; set; }
    
        [Required]
        [DataType(DataType.EmailAddress)]
        public string Email { get; set; }
    }
}