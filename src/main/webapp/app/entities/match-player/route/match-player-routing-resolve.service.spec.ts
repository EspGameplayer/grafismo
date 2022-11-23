import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMatchPlayer, MatchPlayer } from '../match-player.model';
import { MatchPlayerService } from '../service/match-player.service';

import { MatchPlayerRoutingResolveService } from './match-player-routing-resolve.service';

describe('MatchPlayer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MatchPlayerRoutingResolveService;
  let service: MatchPlayerService;
  let resultMatchPlayer: IMatchPlayer | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(MatchPlayerRoutingResolveService);
    service = TestBed.inject(MatchPlayerService);
    resultMatchPlayer = undefined;
  });

  describe('resolve', () => {
    it('should return IMatchPlayer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchPlayer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMatchPlayer).toEqual({ id: 123 });
    });

    it('should return new IMatchPlayer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchPlayer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMatchPlayer).toEqual(new MatchPlayer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MatchPlayer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchPlayer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMatchPlayer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
