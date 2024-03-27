"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useState } from "react";

export default function Fanding() {
  const [dugs, setDugs] = useState("");

  const onClickFanding = () => {
    console.log(dugs);
  };

  return (
    <div className="flex flex-col gap-5">
      <span className="font-medium text-sm">Participate</span>
      <div className="flex items-center text-xs">
        <span className="flex-1">My Dugs</span>
        <span>Charging</span>
      </div>

      <Input
        className="w-full"
        placeholder="Input Dugs"
        value={dugs}
        onChange={(e) => setDugs(e.target.value)}
      />
      <Button onClick={onClickFanding}>Fanding Now</Button>
    </div>
  );
}
