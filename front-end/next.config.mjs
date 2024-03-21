/** @type {import('next').NextConfig} */
import withPWAInit from "@ducanh2912/next-pwa";

const withPWA = withPWAInit({
  dest: "public",
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: "/:path*",
        destination: `http://letter-for.me/:path*`,
      },
    ];
  },
  async redirects() {
    return [
      {
        source: "/auth/oauth-response",
        destination: `http://letter-for.me/tarot`,
        permanent: true,
      },
    ];
  },
};

export default withPWA(nextConfig);
