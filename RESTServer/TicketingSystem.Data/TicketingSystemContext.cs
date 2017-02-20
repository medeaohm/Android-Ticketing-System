using System;
using System.Data.Entity;
using System.Linq;

using Microsoft.AspNet.Identity.EntityFramework;

using TicketingSystem.Data.Models;
using TicketingSystem.Repositories;

namespace TicketingSystem.Data
{
    public class TicketingSystemContext : IdentityDbContext<User>, ITicketingSystemContext
    {
        public TicketingSystemContext()
            : base("DefaultConnection", throwIfV1Schema: false)
        {
        }

        public virtual IDbSet<Ticket> Tickets { get; set; }

        public virtual IDbSet<Transport> Transports { get; set; }

        public virtual IDbSet<Comment> Comments { get; set; }

        public virtual IDbSet<News> News { get; set; }

        public static TicketingSystemContext Create()
        {
            return new TicketingSystemContext();
        }

        public override int SaveChanges()
        {
            this.ApplyAuditInfoRules();
            return base.SaveChanges();
        }

        private void ApplyAuditInfoRules()
        {
            // Approach via @julielerman: http://bit.ly/123661P
            foreach (var entry in
                this.ChangeTracker.Entries()
                    .Where(
                        e =>
                        e.Entity is IAuditInfo && ((e.State == EntityState.Added) || (e.State == EntityState.Modified))))
            {
                var entity = (IAuditInfo)entry.Entity;
                if (entry.State == EntityState.Added && entity.CreatedOn == default(DateTime))
                {
                    entity.CreatedOn = DateTime.Now;
                }
                else
                {
                    entity.ModifiedOn = DateTime.Now;
                }
            }
        }
    }
}
