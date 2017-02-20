using Microsoft.Owin;
using Newtonsoft.Json;
using Owin;
using System.Web.Http;

[assembly: OwinStartup(typeof(TicketingSystem.Startup))]

namespace TicketingSystem
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}