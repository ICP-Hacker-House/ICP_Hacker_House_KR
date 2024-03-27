"use client";

import { BACKEND_BASE_URL } from "@/app/libs/constants";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useToast } from "@/components/ui/use-toast";
import { Loader2 } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function ScanTabs() {
  const [code, setCode] = useState("");
  const [codeActivated, setCodeActivated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const { toast } = useToast();

  const activateCode = async (value: string) => {
    setIsLoading(true);

    try {
      const response = await fetch(`${BACKEND_BASE_URL}/db/set-code`, {
        method: "POST",
        headers: [["Content-Type", "application/json"]],
        body: JSON.stringify({
          code: value,
        }),
      });

      const responseJson: { status: string; code: string } =
        await response.json();
      console.log("🚀 ~ callSetCode ~ responseJson:", responseJson);

      // const response = await fetch(`${BACKEND_BASE_URL}/db/update`, {
      //   method: "POST",
      //   headers: [["Content-Type", "application/json"]],
      //   body: JSON.stringify({
      //     hello: "world",
      //   }),
      // });
      // const responseJson = await response.json();

      setCodeActivated(responseJson.status === "activated" ? true : false);
      toast({
        description: "Your code has been activated successfully!",
      });
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Tabs defaultValue="code" className="w-full">
      <TabsContent value="upload">
        <Link href={"/moments/VLUP-GREB-RAW9/add"}>
          <div className="w-full aspect-video bg-black rounded-lg" />
        </Link>
      </TabsContent>
      <TabsContent value="scan">
        <div className="w-full aspect-video bg-du-gray-200 rounded-lg" />
      </TabsContent>
      <TabsContent value="code">
        <div className="w-full aspect-video flex flex-col gap-4">
          <Label htmlFor="code">Code</Label>
          <Input
            id="code"
            placeholder="xxxx-xxxx-xxxx"
            value={code}
            onChange={async (e) => {
              setCode(e.target.value);
            }}
          />
          {!codeActivated && (
            <Button
              onClick={() => activateCode(code)}
              variant={"secondary"}
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  checking...
                </>
              ) : (
                "Enter Code"
              )}
            </Button>
          )}
          {codeActivated && (
            <Link href={`/moments/${code}/add`}>
              <Button>Add Moment</Button>
            </Link>
          )}
        </div>
      </TabsContent>

      <TabsList className="grid w-full grid-cols-3 mt-5">
        <TabsTrigger value="upload">Upload</TabsTrigger>
        <TabsTrigger value="scan">Scan</TabsTrigger>
        <TabsTrigger value="code">Code</TabsTrigger>
      </TabsList>
    </Tabs>
  );
}
