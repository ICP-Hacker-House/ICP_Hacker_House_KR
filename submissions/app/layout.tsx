import type { Metadata } from "next";
import "./_styles/globals.css";
import Header from "./_components/header";
import localFont from "next/font/local";
import { cn } from "@/components/ui/utils";
import { Toaster } from "@/components/ui/toaster";

export const metadata: Metadata = {
  title: "DugUp",
  description: "DugUp",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={cn([
          Pretendard.className,
          "max-w-96 min-w-[320px] min-h-[calc(100dvh)] w-full bg-fc-white mx-auto pb-10",
        ])}
      >
        <Header />
        {children}
        <Toaster />
      </body>
    </html>
  );
}

const Pretendard = localFont({
  src: [
    {
      path: "./_styles/fonts/PretendardVariable.woff2",
      weight: "400",
      style: "normal",
    },
    {
      path: "./_styles/fonts/PretendardVariable.woff2",
      weight: "500",
      style: "medium",
    },
    {
      path: "./_styles/fonts/PretendardVariable.woff2",
      weight: "600",
      style: "semibold",
    },
    {
      path: "./_styles/fonts/PretendardVariable.woff2",
      weight: "700",
      style: "bold",
    },
    {
      path: "./_styles/fonts/PretendardVariable.woff2",
      weight: "800",
      style: "extrabold",
    },
  ],
  display: "swap",
});
