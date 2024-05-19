import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';
import { NextRequest } from 'next/server';

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico|fonts|images).*)'],
};

export function middleware(request: NextRequest) {
  const { nextUrl, cookies } = request;
  const accessToken = cookies.get('access-token');
  console.log('??????????' + nextUrl);

  if (!accessToken && nextUrl.pathname !== '/login') {
    return NextResponse.redirect(new URL('/login', request.url));
  }

  if (accessToken && nextUrl.pathname === '/login') {
    return NextResponse.redirect(new URL('/'));
  }

  return NextResponse.next();
}
