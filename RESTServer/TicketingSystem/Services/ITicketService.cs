using TicketingSystem.Data.Models;

namespace TicketingSystem.Services
{
    public interface ITicketService
    {
        Ticket Create(int duration, decimal ticketPrice);
    }
}