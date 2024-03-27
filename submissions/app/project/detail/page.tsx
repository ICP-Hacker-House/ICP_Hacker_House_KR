import Image from "next/image";
import Fanding from "./_components/fanding";

export default function Page() {
  return (
    <main className="flex flex-col gap-5 px-6">
      <h1 className="text-xl font-bold">Project Details</h1>

      <Image
        src="/rm.png"
        width={0}
        height={0}
        sizes="100vw"
        className="object-contain rounded-lg w-full aspect-square h-auto"
        alt="Project Mission"
      />

      <span className="font-medium text-xs">BTS</span>
      <span>Happy RM DAY!</span>

      <span className="font-medium text-xs">Goal 500,000</span>
      <span className="font-medium text-xs">Closed Apr 14th 23:59 KST</span>

      <Divider />

      <span className="font-medium text-sm">Mission</span>
      <Image
        src="/project-mission.png"
        width={0}
        height={0}
        sizes="100vw"
        className="object-contain w-full h-auto"
        alt="Project Mission"
      />

      <Divider />

      <span className="font-medium text-sm">Detail</span>
      <p className="text-sm break-words">
        Donate with my IDOL!
        <br /> <br />
        Donate Schedule - After July 16th
        <br />
        Upon failure to reach, Only the used Dug is automatically refunded.
        <br />
        Donations will be made in September.
      </p>

      <Divider />
      <Fanding />

      <Divider />
      <span className="font-medium text-sm">Comments</span>
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
          <span className="text-xs font-medium">Nickname</span>
          <span className="text-xs font-medium">Text Here</span>
        </div>
      </div>
    </main>
  );
}

function Divider() {
  return <div className="border-b border-du-gray-200 w-full my-5" />;
}
