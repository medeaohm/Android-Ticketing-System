using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using TicketingSystem.Data;
using TicketingSystem.Data.Migrations;

namespace TicketingSystem.App_Start
{
    public class DataBaseConfig
    {
        public static void Initialize()
        {
            Database.SetInitializer(new MigrateDatabaseToLatestVersion<TicketingSystemContext, Configuration>());
            TicketingSystemContext.Create().Database.Initialize(true);
        }
    }
}