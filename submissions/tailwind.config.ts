import type { Config } from "tailwindcss";

const config = {
  darkMode: ["class"],
  content: [
    "./pages/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./app/**/*.{ts,tsx}",
    "./src/**/*.{ts,tsx}",
  ],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
      },
      colors: {
        du: {
          gray: {
            900: "rgba(17, 24, 39, 1)",
            800: "rgba(31, 41, 55, 1)",
            600: "rgba(75, 85, 99, 1)",
            400: "rgba(156, 163, 175, 1)",
            300: "rgba(209, 213, 219, 1)",
            200: "rgba(229, 231, 235, 1)",
            100: "rgba(243, 244, 246, 1)",
          },
          red: {
            100: "rgba(227, 0, 0, 1)",
            40: "rgba(255, 242, 244, 1)",
          },
        },
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
} satisfies Config;

export default config;
