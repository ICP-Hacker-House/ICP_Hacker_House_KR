import Community from "./_components/community";
import { communities } from "./_data/communities";

export default function Page() {
  return (
    <main className="flex flex-col items-center gap-5 px-6">
      <h1 className="text-xl font-bold">Communities</h1>
      <div className="flex flex-wrap gap-3">
        {communities.map((community) => (
          <Community key={community.label} {...community} />
        ))}
      </div>
    </main>
  );
}
