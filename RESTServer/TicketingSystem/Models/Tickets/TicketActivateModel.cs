using System.ComponentModel.DataAnnotations;

namespace TicketingSystem.Models.Tickets
{
    public class TicketActivateModel
    {
        [Required]
        public string Id { get; set; }
    }
}