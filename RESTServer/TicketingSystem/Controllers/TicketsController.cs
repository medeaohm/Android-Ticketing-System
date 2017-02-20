using System;
using System.Collections.Generic;
using System.Linq;

using System.Web.Http;
using System.Web.Http.Cors;

using Microsoft.AspNet.Identity;

using TicketingSystem.Data;
using TicketingSystem.Data.Models;
using TicketingSystem.Models;
using TicketingSystem.Models.Tickets;
using TicketingSystem.Services;

namespace TicketingSystem.Controllers
{
    [EnableCors("*", "*", "*")]
    [RoutePrefix("api/Tickets")]
    public class TicketsController : ApiController
    {
        private const decimal InitialTicketPrice = 1.6m;
        private const decimal TicketPricePerHour = 0.1m;
        private const decimal OneWeekInHours = 168;
        private const int DefaultCountTickets = 1000;
        private const decimal DiscountValue = 0.3m; // 30%
        private ITicketingSystemContext context;
        private ITicketService ticketService;

        public TicketsController(ITicketingSystemContext context, ITicketService ticketService)
        {
            this.context = context;
            this.ticketService = ticketService;
        }

        public TicketsController()
            : this(new TicketingSystemContext(), new TicketService())
        {
        }
        
        [HttpGet]
        [Authorize]
        public IHttpActionResult TicketsForCurrentUser()
        {
            string currentUserId = this.User.Identity.GetUserId();

            User currentUser = this.context.Users.Find(currentUserId);
            if (currentUser == null)
            {
                return this.BadRequest("Cannot find current user!");
            }

            IEnumerable<TicketResponseModel> respnseModels = currentUser.Tickets.AsQueryable().MapTicketsToViewModels();
            return this.Json(respnseModels);
        }

        [HttpGet]
        [Route("All")]
        public IHttpActionResult All(int count = DefaultCountTickets)
        {
            if (count <= 0)
            {
                count = DefaultCountTickets;
            }

            IEnumerable<TicketResponseModel> tickets = this.context.Tickets.Take(count).MapTicketsToViewModels().ToList();

            return this.Json(tickets);
        }

        [HttpGet]
        [Route("ById")]
        public IHttpActionResult ById(string id)
        {
            Guid ticketId = Guid.Empty;
            if (!Guid.TryParse(id, out ticketId))
            {
                return this.BadRequest("Invalid Id");
            }
            
            TicketResponseModel ticket = this.context.Tickets.Find(ticketId).MapTicketToViewModel();

            return this.Json(ticket);
        }

        [HttpGet]
        [Route("IsValid")]
        public IHttpActionResult IsValid(string id)
        {
            Guid ticketId = Guid.Empty;
            if (!Guid.TryParse(id, out ticketId))
            {
                return this.BadRequest("Invalid Id");
            }
            
            Ticket ticket = this.context.Tickets.Find(ticketId);
            if (ticket == null)
            {
                return this.BadRequest("Cannot find ticket with id " + ticketId.ToString());
            }

            bool isActive = ticket.DateActivated.HasValue && ticket.ExpiresOn.HasValue &&
                            ticket.ExpiresOn >= DateTime.Now;

            return this.Json(isActive);
        }

        [HttpGet]
        [Route("AllByUserName")]
        public IHttpActionResult AllByUserName(string username, int count = DefaultCountTickets)
        {
            if (string.IsNullOrEmpty(username))
            {
                return this.BadRequest("Username cannot be null or empty");
            }

            if (count <= 0)
            {
                count = DefaultCountTickets;
            }

            IEnumerable<TicketResponseModel> tickets = this.context.Tickets.Where(t => t.Owner.UserName == username).Take(count).MapTicketsToViewModels().ToList();

            return this.Json(tickets);
        }

        [HttpGet]
        [Route("AllByUserId")]
        public IHttpActionResult AllByUserId(string id, int count = DefaultCountTickets)
        {
            if (string.IsNullOrEmpty(id))
            {
                return this.BadRequest("Id cannot be null or empty");
            }

            if (count <= 0)
            {
                count = DefaultCountTickets;
            }

            IEnumerable<TicketResponseModel> tickets = this.context.Tickets.Where(t => t.Owner.Id == id).Take(count).MapTicketsToViewModels().ToList();

            return this.Json(tickets);
        }

        [HttpPost]
        [Authorize]
        [Route("Buy")]
        public IHttpActionResult Buy(TicketBuyModel model)
        {
            int hours = model.Hours;
            string currentUserId = this.User.Identity.GetUserId();
            User user = this.context.Users.FirstOrDefault(u => u.Id == currentUserId);
            
            decimal ticketPrice = ((hours - 1) * TicketPricePerHour) + InitialTicketPrice;
            
            // get discount for one or more weekend tickets
            if (hours > OneWeekInHours)
            {
                ticketPrice = ticketPrice - (ticketPrice * DiscountValue);
            }

            if (user.Balance - ticketPrice < 0)
            {
                return this.BadRequest(string.Format("Not enough money! Ticket price: {0}. Your balance: {1}", ticketPrice, user.Balance));
            }

            Ticket ticket = this.ticketService.Create(hours, ticketPrice);
            user.Balance -= ticketPrice;
            user.Tickets.Add(ticket);
            context.SaveChanges();

            return this.Created("/", new
            {
                QRCode = ticket.QRCode,
                Cost = ticketPrice
            });
        }

        [HttpPut]
        [Route("Activate")]
        public IHttpActionResult Activate(TicketActivateModel model)
        {
            Guid ticketId = Guid.Empty;
            if (!Guid.TryParse(model.Id, out ticketId))
            {
                return this.BadRequest("Invalid Id");
            }

            Ticket ticket =  this.context.Tickets.Find(ticketId);
            if (ticket == null)
            {
                return this.BadRequest(string.Format("Cannot find ticket with id: [{0}] ", ticketId.ToString()));
            }

            if (ticket.DateActivated.HasValue)
            {
                return this.Json(new
                {
                    Message = "Ticked already activated.",
                    ExpiresOn = ticket.ExpiresOn
                });
            }

            ticket.DateActivated = DateTime.Now;
            ticket.ExpiresOn = ticket.DateActivated.Value.AddHours(ticket.DurationInHours);

            this.context.SaveChanges();
            return this.Json(new
            {
                Message = "Successfully activated.",
                ExpiresOn = ticket.ExpiresOn
            });
        }
    }
}
