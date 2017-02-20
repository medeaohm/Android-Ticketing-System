using System.Data.Entity;
using TicketingSystem.Data.Models;

namespace TicketingSystem.Data
{
    public interface ITicketingSystemContext
    {
        IDbSet<Ticket> Tickets { get; set; }

        IDbSet<Transport> Transports { get; set; }

        IDbSet<User> Users { get; set; }

        IDbSet<Comment> Comments { get; set; }

        IDbSet<News> News { get; set; }

        int SaveChanges();
    }
}
