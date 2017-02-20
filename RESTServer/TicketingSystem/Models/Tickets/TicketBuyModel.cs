using System.ComponentModel.DataAnnotations;

namespace TicketingSystem.Models.Tickets
{
    public class TicketBuyModel
    {
        [Required]
        [Range(1, 3000, ErrorMessage = "The hours should be greater than 0")]
        public int Hours { get; set; }
    }
}