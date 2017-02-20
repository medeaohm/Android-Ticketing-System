using System;
using System.Collections.Generic;
using System.Data.Entity.Migrations;
using System.Linq;

using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using TicketingSystem.Data.Constants;
using TicketingSystem.Data.Models;

namespace TicketingSystem.Data.Migrations
{
    public class Configuration : DbMigrationsConfiguration<TicketingSystemContext>
    {
  

        public Configuration()
        {
            this.AutomaticMigrationsEnabled = true;
            this.AutomaticMigrationDataLossAllowed = true;
        }

        protected override void Seed(TicketingSystemContext context)
        {
            var userManager = new UserManager<User>(new UserStore<User>(context));
            var roleManager = new RoleManager<IdentityRole>(new RoleStore<IdentityRole>(context));

            if (context.Roles.Count() == 0)
            {
                this.AddRolesIfNotExist(roleManager, DataModelConstants.AdminRole, DataModelConstants.InspectorRole);
            }

            const string DefaultLastName = "Admin";
            const decimal DefaultBalance = 300;

            var user1 = new User() {
                UserName = "admin",
                Email = "admin@email.com",
                FirstName = "Angular",
                LastName = DefaultLastName,
                Balance = DefaultBalance
            };
            var user2 = new User() {
                UserName = "John",
                Email = "test@email.com",
                FirstName = "John",
                LastName = DefaultLastName,
                Balance = DefaultBalance
            };
            var user3 = new User() {
                UserName = "batman",
                Email = "bat@email.com",
                FirstName = "Bruce",
                LastName = DefaultLastName,
                Balance = DefaultBalance
            };

            if (context.Users.Count() == 0)
            {
                ICollection<User> defaultAdmins = new List<User>();
                defaultAdmins.Add(user1);
                defaultAdmins.Add(user2);
                defaultAdmins.Add(user3);

                this.AddAdminsIfNotExist(userManager, defaultAdmins);
            }

            List<Comment> comments1 = new List<Comment>();
            List<Comment> comments2 = new List<Comment>();

            var comment1 = new Comment() {
                Content = "Thanks!",
                Author = user1,
                CreatedOn = new DateTime(2016, 12, 25),
            };
            var comment2 = new Comment() {
                Content = "Hello and Merry Christmass!",
                Author = user3,
                CreatedOn = new DateTime(2016, 12, 27),
            };
            var comment3 = new Comment() {
                Content = "I whish you a happy new year!",
                Author = user2,
                CreatedOn = new DateTime(2016, 12, 20),
            };

            comments1.Add(comment1);
            comments1.Add(comment2);

            comments2.Add(comment3);

            if (context.News.Count() == 0)
            {
                string authorId = context.Users.Select(u => u.Id).FirstOrDefault();

                var news1 = new News() {
                    Title = "Merry Christmass",
                    Content = "Merry Christmass Merry ChristmassMerry ChristmassMerry Christmass Merry ChristmassMerry Christmass Merry Christmass Merry Christmass Merry Christmass",
                    CreatedOn = new DateTime(2016, 12, 20),
                    AuthorId = authorId,
                    Comments = comments2
                };

                var news2 = new News() {
                    Title = "Happy New Year",
                    Content = " Happy New Year  Happy New Year  Happy New Year  Happy New Year vvv Happy New Year  Happy New Year  Happy New Year  Happy New Year  Happy New Year  Happy New Year  Happy New Year  Happy New Year  Happy New Year v Happy New Year ",
                    CreatedOn = new DateTime(2016, 12, 21),
                    AuthorId = authorId,
                    Comments = comments1
                };

                context.News.Add(news1);
                context.News.Add(news2);
                context.SaveChanges();
            }
        }

        private void AddRolesIfNotExist(RoleManager<IdentityRole> roleManager, params string[] roles)
        {
            foreach (string roleName in roles)
            {
                if (!roleManager.RoleExists(roleName))
                {
                    IdentityResult result = roleManager.Create(new IdentityRole(roleName));
                    this.ValidateResult(result);
                }
            }
        }

        private void AddAdminsIfNotExist(UserManager<User> userManager, IEnumerable<User> users)
        {
            foreach (User currentUser in users)
            {
                if (userManager.FindByName(currentUser.UserName) == null)
                {
                    IdentityResult result = userManager.Create(currentUser, DataModelConstants.DefaultPassword);
                    this.ValidateResult(result);
                    IdentityResult addToRoleResult = userManager.AddToRole(currentUser.Id, DataModelConstants.AdminRole);
                    this.ValidateResult(result);
                }
            }
        }

        private void ValidateResult(IdentityResult result)
        {
            if (!result.Succeeded)
            {
                string message = string.Join(", ", result.Errors);
                throw new InvalidOperationException(message);
            }
        }
    }
}
