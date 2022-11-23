import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMatchStats, MatchStats } from '../match-stats.model';
import { MatchStatsService } from '../service/match-stats.service';

import { MatchStatsRoutingResolveService } from './match-stats-routing-resolve.service';

describe('MatchStats routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MatchStatsRoutingResolveService;
  let service: MatchStatsService;
  let resultMatchStats: IMatchStats | undefined;

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
    routingResolveService = TestBed.inject(MatchStatsRoutingResolveService);
    service = TestBed.inject(MatchStatsService);
    resultMatchStats = undefined;
  });

  describe('resolve', () => {
    it('should return IMatchStats returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchStats = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMatchStats).toEqual({ id: 123 });
    });

    it('should return new IMatchStats if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchStats = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMatchStats).toEqual(new MatchStats());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MatchStats })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMatchStats = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMatchStats).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
