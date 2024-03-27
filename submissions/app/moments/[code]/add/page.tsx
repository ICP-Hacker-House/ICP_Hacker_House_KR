"use client";

import { BACKEND_BASE_URL } from "@/app/libs/constants";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { useEffect, useState } from "react";

interface Props {
  params: {
    code: string;
  };
}

export default function Page({ params }: Props) {
  const [pageContent, setPageContent] = useState<{
    text: string;
    title: string;
  }>();

  const fetchInitialContent = async (code: string) => {
    try {
      const response = await fetch(`${BACKEND_BASE_URL}/db/moment`, {
        method: "POST",
        headers: [["Content-Type", "application/json"]],
        body: JSON.stringify({
          code: code,
        }),
      });

      const responseJson: { text: string; title: string } =
        await response.json();
      console.log("🚀 ~ callSetCode ~ responseJson:", responseJson);

      setPageContent(responseJson);
    } catch (error) {
      console.error(error);
    }
  };
  useEffect(() => {
    fetchInitialContent(params.code);
  }, []);

  return (
    <main className="flex flex-col gap-7 items-center px-6">
      {pageContent ? (
        <>
          <span className="font-bold text-xl">{pageContent?.text}</span>
          <span className="font-medium text-sm">{pageContent?.title}</span>
          <Button variant={"secondary"} disabled>
            {params.code}
          </Button>
          <Link href={`/moments/${params.code}/add/success`} className="w-full">
            <Button>Add to my moments</Button>
          </Link>
        </>
      ) : (
        <div>loading...</div>
      )}
    </main>
  );
}
