/** @type {import('next').NextConfig} */
import withPWAInit from "@ducanh2912/next-pwa";

const withPWA = withPWAInit({
  dest: "public",
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async headers() {
    return [
      {
        source: `/:path*`,
        headers: [
          {
            key: "Access-Control-Allow-Origin",
            value: "https://localhost:3001",
          },
          {
            key: "Access-Control-Allow-Methods",
            value: "GET, POST, PUT, DELETE, OPTIONS",
          },
          {
            key: "Access-Control-Allow-Headers",
            value: "Content-Type, Authorization",
          },
          {
            key: "Access-Control-Allow-Credentials",
            value: "true",
          },
        ],
      },
    ];
  },
  async rewrites() {
    return [
      {
        source: "/:path*",
        destination: `${process.env.NEXT_PUBLIC_API_URL}/:path*`,
      },
    ];
  },

  async redirects() {
    return [
      {
        source: "/auth/oauth-response",
        permanent: true,
        destination: `${process.env.NEXT_PUBLIC_URL}`,
      },
    ];
  },
  images: {
    domains: ["ssafy-tarot-01.s3.ap-northeast-2.amazonaws.com"],
  },
};

export default withPWA(nextConfig);
