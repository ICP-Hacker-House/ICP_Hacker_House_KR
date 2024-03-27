import { Button } from "@/components/ui/button";
import OrDivider from "../_components/or-divider";
import SocialButtons from "../_components/social-buttons";
import Link from "next/link";

export default function Page() {
  return (
    <main className="flex flex-col gap-7 items-center">
      <span className="font-bold text-xl">Welcome!</span>
      <SocialButtons />
      <OrDivider />
      <Link href="/accounts/verify/email" className="w-full">
        <Button>Create your account</Button>
      </Link>
    </main>
  );
}
