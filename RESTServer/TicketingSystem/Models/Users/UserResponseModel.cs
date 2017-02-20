using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TicketingSystem.Models.Users
{
    public class UserResponseModel
    {
        public string Id { get; set; }

        public string UserName { get; set; }

        public string FullName { get; set; }
    }
}