/** @type {import('next').NextConfig} */
import withPWAInit from "@ducanh2912/next-pwa";

const withPWA = withPWAInit({
  dest: "public",
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async redirects() {
    return [
      {
        source: "/auth/oauth-response",
        destination: "https://dev.letter-for.me/",
        permanent: true,
      },
    ];
  },
};

export default withPWA(nextConfig);
