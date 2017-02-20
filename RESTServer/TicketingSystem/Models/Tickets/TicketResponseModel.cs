using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using TicketingSystem.Models.Users;

namespace TicketingSystem.Models.Tickets
{
    public class TicketResponseModel
    {
        public string Id { get; set; }

        public DateTime BoughtAt { get; set; }

        public decimal Cost { get; set; }

        public bool Expired { get; set; }

        public bool Activated { get; set; }

        public DateTime? DateActivated { get; set; }

        public DateTime? ExpiresOn { get; set; }

        public int Duration { get; set; }

        public byte[] QRCode { get; set; }

        public UserResponseModel Owner { get; set; }
    }
}