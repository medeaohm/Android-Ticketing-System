namespace TicketingSystem.Models.Users
{
    using System.ComponentModel.DataAnnotations;

    public class UserDeleteModel
    {
        [Required]
        public string Id { get; set; }
    }
}