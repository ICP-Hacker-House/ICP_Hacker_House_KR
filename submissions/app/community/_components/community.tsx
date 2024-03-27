import Image from "next/image";
import Link from "next/link";

interface Props {
  image: string;
  label: string;
}

export default function Community({ image, label }: Props) {
  return (
    <Link
      className="flex flex-col gap-4 items-center"
      href={`/community/${label}`}
    >
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
