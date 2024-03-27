import ScanTabs from "./_components/scan-tabs";

export default function Page() {
  return (
    <main className="flex flex-col gap-7 items-center px-6">
      <span className="font-bold text-xl">Scan the QR code on your card</span>
      <ScanTabs />
    </main>
  );
}
