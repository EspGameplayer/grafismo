import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICompetitionPlayer, CompetitionPlayer } from '../competition-player.model';
import { CompetitionPlayerService } from '../service/competition-player.service';

import { CompetitionPlayerRoutingResolveService } from './competition-player-routing-resolve.service';

describe('CompetitionPlayer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CompetitionPlayerRoutingResolveService;
  let service: CompetitionPlayerService;
  let resultCompetitionPlayer: ICompetitionPlayer | undefined;

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
    routingResolveService = TestBed.inject(CompetitionPlayerRoutingResolveService);
    service = TestBed.inject(CompetitionPlayerService);
    resultCompetitionPlayer = undefined;
  });

  describe('resolve', () => {
    it('should return ICompetitionPlayer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompetitionPlayer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompetitionPlayer).toEqual({ id: 123 });
    });

    it('should return new ICompetitionPlayer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompetitionPlayer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCompetitionPlayer).toEqual(new CompetitionPlayer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CompetitionPlayer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompetitionPlayer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompetitionPlayer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
