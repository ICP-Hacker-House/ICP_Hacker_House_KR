import {
  Accordion,
  AccordionItem,
  AccordionTrigger,
  AccordionContent,
} from "@/components/ui/accordion";
import Image from "next/image";
import Link from "next/link";
import { communities } from "../_data/communities";

interface Props {
  params: {
    name: string;
  };
}

export default function Page({ params }: Props) {
  return (
    <main className="flex flex-col items-center gap-5 px-6">
      <h1 className="text-xl font-bold">{params.name}</h1>
      <div className="flex items-center gap-2 w-full">
        <Image
          src="/profile-unsplash.jpg"
          width={56}
          height={56}
          className="object-cover rounded-full aspect-square"
          alt="profile"
          quality={100}
        />
        <div className="flex flex-col flex-1">
          <span className="text-sm font-bold">Nickname</span>
          <span className="text-sm font-medium">@BA392098f47</span>
        </div>
      </div>
      <div className="rounded-lg border-du-gray-200 border w-full h-12 text-sm font-medium flex items-center justify-center text-du-gray-400">
        $DUGS
      </div>
      <span className="text-du-gray-600 text-xs">Joined April 16th 2024</span>

      <Accordion
        type="single"
        collapsible
        className="w-full"
        defaultValue="item-2"
      >
        <AccordionItem value="item-1">
          <AccordionTrigger>6 Moment</AccordionTrigger>
          <AccordionContent>
            <Image
              src="/moments.png"
              width={0}
              height={0}
              sizes="100vw"
              className="object-contain w-full h-auto"
              alt="Project Mission"
              quality={100}
            />
          </AccordionContent>
        </AccordionItem>
        <AccordionItem value="item-2">
          <AccordionTrigger>4 Project</AccordionTrigger>
          <AccordionContent className="flex flex-wrap gap-5">
            {communities.map((community) => (
              <Project key={community.label} {...community} />
            ))}
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </main>
  );
}

function Project({ label, image }: { label: string; image: string }) {
  return (
    <Link className="flex flex-col gap-4 items-center" href={`/project/detail`}>
      <Image
        src={image}
        width={90}
        height={90}
        className="object-cover rounded-full aspect-square"
        alt={label}
        quality={100}
      />
      <span className="font-medium text-xs">{label}</span>
    </Link>
  );
}
