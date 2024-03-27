import { Button } from "@/components/ui/button";

export default function SocialButtons() {
  return (
    <div className="flex flex-col gap-3 w-full">
      <Button variant="secondary">Continue with Google</Button>
      <Button variant="secondary">Continue with X</Button>
    </div>
  );
}
