"use client";

import { BACKEND_BASE_URL } from "@/app/libs/constants";
import { PlusIcon } from "lucide-react";
import Link from "next/link";
import { useEffect, useState } from "react";
import Community from "../community/_components/community";
import { communities } from "../community/_data/communities";
import { Skeleton } from "@/components/ui/skeleton";

interface Moment {
  text: string;
  title: string;
  artist: string;
}
[];

export default function Page() {
  const [pageContent, setPageContent] = useState<Moment[]>();

  const fetchInitialContent = async (code: string) => {
    try {
      const response = await fetch(`${BACKEND_BASE_URL}/db/my`, {
        method: "POST",
        headers: [["Content-Type", "application/json"]],
        body: JSON.stringify({
          code: code,
        }),
      });

      const responseJson: Moment[] = await response.json();
      console.log("🚀 ~ callSetCode ~ responseJson:", responseJson);

      setPageContent(responseJson);
    } catch (error) {
      console.error(error);
    }
  };
  useEffect(() => {
    fetchInitialContent("user123");
  }, []);

  return (
    <main className="flex flex-col py-5 gap-5 px-6">
      <h1 className="font-bold text-xl">Hello, Dug!</h1>
      <span className="font-bold text-sm">My moments</span>

      {pageContent ? (
        pageContent.map((moment, index) => (
          <div key={index} className="flex gap-3 shadow rounded-lg p-5">
            <div className="w-20 aspect-square bg-du-gray-200 rounded-lg" />
            <div className="flex flex-col gap-2">
              <span className="font-bold text-sm">{moment.text}</span>
              <span className="font-medium text-sm">{moment.title}</span>
              <span className="text-du-gray-400 font-bold text-sm">
                {moment.artist}
              </span>
            </div>
          </div>
        ))
      ) : (
        <SkeletonDemo />
      )}

      <Link
        className="flex items-center justify-between mt-10"
        href="/community"
      >
        <span className="font-bold text-sm">My Communities</span>
        <PlusIcon size={24} />
      </Link>
      <div className="flex flex-wrap gap-3">
        {communities.map((community) => (
          <Community key={community.label} {...community} />
        ))}
      </div>
    </main>
  );
}

export function SkeletonDemo() {
  return (
    <div className="flex items-center space-x-4">
      <Skeleton className="h-12 w-12 rounded-full" />
      <div className="space-y-2">
        <Skeleton className="h-4 w-[250px]" />
        <Skeleton className="h-4 w-[200px]" />
      </div>
    </div>
  );
}
