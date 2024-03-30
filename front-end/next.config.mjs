/** @type {import('next').NextConfig} */
import withPWAInit from '@ducanh2912/next-pwa';

const withPWA = withPWAInit({
  dest: 'public',
});

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: '/:path*',
        destination: `${process.env.NEXT_PUBLIC_API_URL}/:path*`,
      },
    ];
  },

  async redirects() {
    return [
      {
        source: '/auth/oauth-response',
        permanent: true,
        destination: `${process.env.NEXT_PUBLIC_URL}`,
      },
    ];
  },
};

export default withPWA(nextConfig);
