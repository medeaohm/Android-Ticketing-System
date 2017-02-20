namespace TicketingSystem.Data.Models
{
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using Constants;

    public class Transport
    {
        private ICollection<Ticket> tickets;

        public Transport()
        {
            this.tickets = new HashSet<Ticket>();
        }

        [Key]
        public int Id { get; set; }

        //// It is string because - it can be 10TM s
        [MaxLength(DataModelConstants.StringShortMaxLength)]
        public string LineNumber { get; set; }

        public TransportType TransportType { get; set; }

        public ICollection<Ticket> Tickets
        {
            get { return this.tickets; }
            set { this.tickets = value; }
        }
    }
}
