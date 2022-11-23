import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILocalAssociationProvince, LocalAssociationProvince } from '../local-association-province.model';
import { LocalAssociationProvinceService } from '../service/local-association-province.service';

import { LocalAssociationProvinceRoutingResolveService } from './local-association-province-routing-resolve.service';

describe('LocalAssociationProvince routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LocalAssociationProvinceRoutingResolveService;
  let service: LocalAssociationProvinceService;
  let resultLocalAssociationProvince: ILocalAssociationProvince | undefined;

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
    routingResolveService = TestBed.inject(LocalAssociationProvinceRoutingResolveService);
    service = TestBed.inject(LocalAssociationProvinceService);
    resultLocalAssociationProvince = undefined;
  });

  describe('resolve', () => {
    it('should return ILocalAssociationProvince returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLocalAssociationProvince = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLocalAssociationProvince).toEqual({ id: 123 });
    });

    it('should return new ILocalAssociationProvince if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLocalAssociationProvince = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLocalAssociationProvince).toEqual(new LocalAssociationProvince());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LocalAssociationProvince })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLocalAssociationProvince = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLocalAssociationProvince).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
