using System;
using System.Drawing.Imaging;
using System.IO;
using Gma.QrCodeNet.Encoding;
using Gma.QrCodeNet.Encoding.Windows.Render;
using TicketingSystem.Data.Models;

namespace TicketingSystem.Services
{
    public class TicketService : ITicketService
    {
        public Ticket Create(int durationHours, decimal ticketPrice)
        {
            Ticket ticket = new Ticket();
            ticket.BoughtAt = DateTime.Now;
            ticket.DurationInHours = durationHours;
            ticket.Cost = ticketPrice;
            ticket.QRCode = this.GenerateQrCode(ticket.Id.ToString());
            return ticket;
        }

        private byte[] GenerateQrCode(string value)
        {
            QrEncoder encoder = new QrEncoder(ErrorCorrectionLevel.M);
            QrCode code = encoder.Encode(value.ToString());

            byte[] result = null;
            using (MemoryStream stream = new MemoryStream())
            {
                var render = new GraphicsRenderer(new FixedModuleSize(12, QuietZoneModules.Two));
                render.WriteToStream(code.Matrix, ImageFormat.Png, stream);
                result = stream.GetBuffer();
            }

            return result;
        }
    }
}
