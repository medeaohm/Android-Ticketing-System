using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
//using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using TicketingSystem.App_Start;

namespace TicketingSystem
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        public WebApiApplication()
        {
            this.EndRequest += WebApiApplication_EndRequest;
        }

        private void WebApiApplication_EndRequest(object sender, EventArgs e)
        {
            if (!this.Response.Headers.AllKeys.Contains("Access-Control-Allow-Origin"))
            {
                this.Response.AddHeader("Access-Control-Allow-Origin", "*");
            }
        }

        protected void Application_Start()
        {
            //AreaRegistration.RegisterAllAreas();
            GlobalConfiguration.Configure(WebApiConfig.Register);
            //FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            //RouteConfig.RegisterRoutes(RouteTable.Routes);
            //BundleConfig.RegisterBundles(BundleTable.Bundles);
            DataBaseConfig.Initialize();
        }
    }
}
