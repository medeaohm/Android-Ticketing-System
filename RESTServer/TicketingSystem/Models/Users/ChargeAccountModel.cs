namespace TicketingSystem.Models.Users
{
    using System.ComponentModel.DataAnnotations;

    public class ChargeAccountModel
    {
        [Required]
        public string CardNumber { get; set; }

        [Required]
        public string SecurityCode { get; set; }

        // Not implemented
        public string CardType { get; set; }

        [Required]
        [Range(1,12)]
        public byte ExpireMonth { get; set; }

        [Required]
        public int ExpireYear { get; set; }

        [Required]
        public string CardHolderNames { get; set; }

        [Required]
        public decimal Amount { get; set; }
    }
}