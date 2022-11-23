import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGraphicElementPos, GraphicElementPos } from '../graphic-element-pos.model';
import { GraphicElementPosService } from '../service/graphic-element-pos.service';

import { GraphicElementPosRoutingResolveService } from './graphic-element-pos-routing-resolve.service';

describe('GraphicElementPos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GraphicElementPosRoutingResolveService;
  let service: GraphicElementPosService;
  let resultGraphicElementPos: IGraphicElementPos | undefined;

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
    routingResolveService = TestBed.inject(GraphicElementPosRoutingResolveService);
    service = TestBed.inject(GraphicElementPosService);
    resultGraphicElementPos = undefined;
  });

  describe('resolve', () => {
    it('should return IGraphicElementPos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGraphicElementPos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGraphicElementPos).toEqual({ id: 123 });
    });

    it('should return new IGraphicElementPos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGraphicElementPos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGraphicElementPos).toEqual(new GraphicElementPos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GraphicElementPos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGraphicElementPos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGraphicElementPos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
