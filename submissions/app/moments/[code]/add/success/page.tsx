import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function Page() {
  return (
    <main className="flex flex-col gap-7 items-center px-6">
      <span className="font-bold text-xl text-center">
        Your badge
        <br />
        successfully added.
      </span>

      <div className="w-full flex gap-3 shadow rounded-lg p-5">
        <div className="w-20 aspect-square bg-du-gray-200 rounded-lg" />
        <div className="flex flex-col gap-2">
          <span className="font-bold text-sm">I’ll be by your side</span>
          <span className="font-medium text-sm">BOBBY</span>
          <span className="text-du-gray-900 font-medium text-sm">
            2024.03.10 11:00
          </span>
        </div>
      </div>

      <Link href={"/my"} className="w-full">
        <Button>See moment detail</Button>
      </Link>
    </main>
  );
}
