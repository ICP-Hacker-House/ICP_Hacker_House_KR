import { Button } from "@/components/ui/button";
import Image from "next/image";
import Link from "next/link";

export default function Header() {
  return (
    <header className="sticky top-0 z-50 w-ful h-[72px] bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/80 flex items-center justify-between px-6">
      <Link href="/">
        <Image src="/logo.png" alt="DugUp" width={116} height={32} quality={100}/>
      </Link>

      <Link href="/qr/scan">
        <Button variant="secondary" className="w-[42px] h-[42px] p-0">
          <Image src="/qr-scan.svg" alt="QR" width={24} height={24} />
        </Button>
      </Link>
    </header>
  );
}
