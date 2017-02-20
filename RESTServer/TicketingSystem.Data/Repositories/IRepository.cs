namespace TicketingSystem.Repositories
{
    using System;
    using System.Linq;

    public interface IRepository<T> : IDisposable
        where T : class
    {
        IQueryable<T> All();

        T GetById(object id);

        void Add(T entity);

        void Update(T entity);

        T Delete(T entity);

        T Delete(object id);

        void Detach(T entity);

        int SaveChanges();
    }
}
